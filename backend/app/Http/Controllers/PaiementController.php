<?php

namespace App\Http\Controllers;

use App\Models\Eleve;
use Barryvdh\DomPDF\Facade\Pdf;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class PaiementController extends Controller
{
    public function index(Request $request, Eleve $eleve)
    {
        if (! $request->user()->eleves()->where('eleves.id', $eleve->id)->exists()) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $versements = $eleve->paiements()->latest('date')->get()->map(fn($p) => [
            'id' => $p->id,
            'date' => $p->date,
            'montant' => $p->montant,
            'mode_paiement' => $p->mode_paiement,
            'recu_url' => $p->recu_path ? asset('storage/' . $p->recu_path) : null,
        ]);

        $montantPaye = $eleve->paiements()->sum('montant');

        return response()->json([
            'montant_total_du' => $eleve->montant_total_du,
            'montant_paye' => $montantPaye,
            'montant_restant' => max(0, $eleve->montant_total_du - $montantPaye),
            'versements' => $versements,
        ]);
    }

    public function store(Request $request, Eleve $eleve)
    {
        if (! $request->user()->eleves()->where('eleves.id', $eleve->id)->exists()) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $data = $request->validate([
            'montant' => ['required', 'integer', 'min:1'],
            'mode_paiement' => ['required', 'string'],
        ]);

        $paiement = $eleve->paiements()->create([
            'montant' => $data['montant'],
            'date' => now()->toDateString(),
            'mode_paiement' => $data['mode_paiement'],
        ]);

        $pdf = Pdf::loadView('pdf.recu', [
            'paiement' => $paiement,
            'eleve' => $eleve,
            'parent' => $request->user(),
        ]);
        $filename = "recus/recu_{$paiement->id}.pdf";
        Storage::disk('public')->put($filename, $pdf->output());
        $paiement->update(['recu_path' => $filename]);

        return response()->json([
            'id' => $paiement->id,
            'date' => $paiement->date,
            'montant' => $paiement->montant,
            'mode_paiement' => $paiement->mode_paiement,
            'recu_url' => asset('storage/' . $filename),
        ], 201);
    }
}