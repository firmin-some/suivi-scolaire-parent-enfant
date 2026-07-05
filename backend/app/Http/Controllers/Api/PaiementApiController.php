<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Eleve;
use App\Models\Paiement;
use Barryvdh\DomPDF\Facade\Pdf;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class PaiementApiController extends Controller
{
    public function index(Request $request, Eleve $eleve)
    {
        if ($eleve->parent_id !== $request->user()->id) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $fraisTotal = $eleve->classe->frais ?? 0;
        $montantPaye = $eleve->paiements()->sum('montant');
        $montantRestant = max(0, $fraisTotal - $montantPaye);

        $versements = $eleve->paiements()->latest('date_paiement')->get()->map(fn($p) => [
            'id' => $p->id,
            'date' => $p->date_paiement,
            'montant' => $p->montant,
            'mode_paiement' => $p->mode_paiement,
            'recu_url' => $p->recu_path ? asset('storage/' . $p->recu_path) : null,
        ]);

        return response()->json([
            'montant_total_du' => $fraisTotal,
            'montant_paye' => $montantPaye,
            'montant_restant' => $montantRestant,
            'versements' => $versements,
        ]);
    }

    public function store(Request $request, Eleve $eleve)
    {
        if ($eleve->parent_id !== $request->user()->id) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $data = $request->validate([
            'montant' => ['required', 'integer', 'min:1'],
            'mode_paiement' => ['required', 'string'],
        ]);

        $paiement = $eleve->paiements()->create([
            'montant' => $data['montant'],
            'date_paiement' => now()->toDateString(),
            'mode_paiement' => $data['mode_paiement'],
            'statut' => 'en_attente',
        ]);
$totalPaye = $eleve->paiements()->sum('montant');

$reste = max(
    0,
    ($eleve->classe->frais ?? 0) - $totalPaye
);

$pdf = Pdf::loadView('pdf.recu', [
    'paiement' => $paiement,
    'eleve' => $eleve,
    'parent' => $request->user(),
    'classe' => $eleve->classe,
    'totalPaye' => $totalPaye,
    'reste' => $reste,
]);
        $filename = "recus/recu_{$paiement->id}.pdf";
        Storage::disk('public')->put($filename, $pdf->output());
        $paiement->update(['recu_path' => $filename]);

        return response()->json([
            'id' => $paiement->id,
            'date' => $paiement->date_paiement,
            'montant' => $paiement->montant,
            'mode_paiement' => $paiement->mode_paiement,
            'recu_url' => asset('storage/' . $filename),
        ], 201);
    }
}