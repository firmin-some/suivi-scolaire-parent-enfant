<?php

namespace App\Http\Controllers;

use App\Models\Eleve;
use Barryvdh\DomPDF\Facade\Pdf;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class BulletinController extends Controller
{
    public function show(Request $request, Eleve $eleve)
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
                $moyenne = $notes->sum(fn($n) => $n->valeur * $n->coefficient) / max($notes->sum('coefficient'), 1);
                return ['nom' => $matiere->nom, 'moyenne' => round($moyenne, 2)];
            })->values();

        $moyenneGenerale = round($matieres->avg('moyenne') ?? 0, 2);

        $pdf = Pdf::loadView('pdf.bulletin', [
            'eleve' => $eleve,
            'trimestre' => $trimestre,
            'matieres' => $matieres,
            'moyenneGenerale' => $moyenneGenerale,
        ]);

        $filename = "bulletins/eleve_{$eleve->id}_trim_{$trimestre}.pdf";
        Storage::disk('public')->put($filename, $pdf->output());

        return response()->json(['bulletin_url' => asset('storage/' . $filename)]);
    }
}