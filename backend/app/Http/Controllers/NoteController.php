<?php

namespace App\Http\Controllers;

use App\Models\Eleve;
use Illuminate\Http\Request;

class NoteController extends Controller
{
    public function index(Request $request, Eleve $eleve)
    {
        if (! $request->user()->eleves()->where('eleves.id', $eleve->id)->exists()) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $trimestre = $request->query('trimestre', 1);

        $matieres = $eleve->notes()
            ->with('matiere')
            ->where('trimestre', $trimestre)
            ->get()
            ->groupBy('matiere_id')
            ->map(function ($notes) {
                $matiere = $notes->first()->matiere;
                $moyennePonderee = $notes->sum(fn($n) => $n->valeur * $n->coefficient)
                    / max($notes->sum('coefficient'), 1);

                return [
                    'matiere' => $matiere->nom,
                    'notes' => $notes->map(fn($n) => [
                        'type' => $n->type,
                        'valeur' => (float) $n->valeur,
                        'coefficient' => $n->coefficient,
                        'date' => $n->date,
                    ])->values(),
                    'moyenne' => round($moyennePonderee, 2),
                ];
            })->values();

        return response()->json([
            'trimestre' => (int) $trimestre,
            'matieres' => $matieres,
        ]);
    }
}