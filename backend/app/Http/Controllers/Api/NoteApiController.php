<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Eleve;
use Barryvdh\DomPDF\Facade\Pdf;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class NoteApiController extends Controller
{
    public function index(Request $request, Eleve $eleve)
    {
        if ($eleve->parent_id !== $request->user()->id) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $trimestre = $request->query('trimestre', 'T1');
        // Accepte aussi le format numérique (1, 2, 3) envoyé par l'app Android
        if (is_numeric($trimestre)) {
            $trimestre = 'T' . $trimestre;
        }

        $notes = $eleve->notes()
            ->where('trimestre', $trimestre)
            ->get()
            ->groupBy('matiere')
            ->map(function ($groupe, $matiere) {
                $moyenne = round($groupe->avg('note'), 2);
                return [
                    'matiere' => $matiere,
                    'notes' => $groupe->map(fn($n) => [
                        'type' => 'Note',
                        'valeur' => (float) $n->note,
                        'coefficient' => 1,
                        'date' => $n->created_at->toDateString(),
                    ])->values(),
                    'moyenne' => $moyenne,
                ];
            })->values();

        return response()->json([
            'trimestre' => (int) str_replace('T', '', $trimestre),
            'matieres' => $notes,
        ]);
    }

    public function bulletin(Request $request, Eleve $eleve)
    {
        if ($eleve->parent_id !== $request->user()->id) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $trimestre = $request->query('trimestre', 1);
        $trimestreLabel = 'T' . $trimestre;

        $matieres = $eleve->notes()
            ->where('trimestre', $trimestreLabel)
            ->get()
            ->groupBy('matiere')
            ->map(fn($g, $m) => ['nom' => $m, 'moyenne' => round($g->avg('note'), 2)])
            ->values();

        $moyenneGenerale = round($matieres->avg('moyenne') ?? 0, 2);

        $pdf = Pdf::loadView('pdf.bulletin', [
            'eleve' => $eleve,
            'trimestre' => $trimestre,
            'matieres' => $matieres,
            'moyenneGenerale' => $moyenneGenerale,
            'classe' => $eleve->classe,
        ]);

        $filename = "bulletins/eleve_{$eleve->id}_trim_{$trimestre}.pdf";
        Storage::disk('public')->put($filename, $pdf->output());

        return response()->json(['bulletin_url' => asset('storage/' . $filename)]);
    }
}