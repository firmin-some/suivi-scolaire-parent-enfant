<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Eleve;
use Illuminate\Http\Request;

class EleveApiController extends Controller
{
    public function index(Request $request)
    {
        $eleves = Eleve::where('parent_id', $request->user()->id)
            ->with('classe')
            ->get()
            ->map(fn($e) => [
                'id' => $e->id,
                'nom' => $e->nom,
                'prenom' => $e->prenom,
                'photo_url' => $e->photo ? asset('storage/' . $e->photo) : null,
                'classe' => $e->classe->nom ?? $e->classe->niveau ?? '',
            ]);

        return response()->json($eleves);
    }

    public function verify(Request $request)
    {
        $data = $request->validate([
            'nom' => ['required', 'string'],
            'classe' => ['required', 'string'],
        ]);

        $eleve = Eleve::where('parent_id', $request->user()->id)
            ->whereRaw('LOWER(nom) = ?', [mb_strtolower($data['nom'])])
            ->whereHas('classe', function ($q) use ($data) {
                $q->whereRaw('LOWER(niveau) = ?', [mb_strtolower($data['classe'])])
                  ->orWhereRaw('LOWER(nom) = ?', [mb_strtolower($data['classe'])]);
            })
            ->with('classe')
            ->first();

        if (! $eleve) {
            return response()->json(['message' => 'Aucun enfant ne correspond à ces informations.'], 404);
        }

        return response()->json([
            'id' => $eleve->id,
            'nom' => $eleve->nom,
            'prenom' => $eleve->prenom,
            'photo_url' => $eleve->photo ? asset('storage/' . $eleve->photo) : null,
            'classe' => $eleve->classe->niveau ?? '',
        ]);
    }

    public function show(Request $request, Eleve $eleve)
    {
        if ($eleve->parent_id !== $request->user()->id) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $notes = $eleve->notes ?? collect();
        $moyenneGenerale = $notes->count() > 0 ? round($notes->avg('note'), 2) : 0;

        $elevesClasse = Eleve::where('classe_id', $eleve->classe_id)->pluck('id');
        $moyennesClasse = $elevesClasse->map(function ($eid) {
            $moy = \App\Models\Note::where('eleve_id', $eid)->avg('note');
            return ['id' => $eid, 'moy' => round($moy ?? 0, 2)];
        })->sortByDesc('moy')->values();

        $rang = $moyennesClasse->search(fn($m) => $m['id'] === $eleve->id) + 1;

        $dernieresNotes = $notes->sortByDesc('created_at')->take(5)->map(fn($n) => [
            'matiere' => $n->matiere,
            'valeur' => (float) $n->note,
            'date' => $n->created_at->toDateString(),
        ])->values();

        return response()->json([
            'id' => $eleve->id,
            'nom' => $eleve->nom,
            'prenom' => $eleve->prenom,
            'photo_url' => $eleve->photo ? asset('storage/' . $eleve->photo) : null,
            'classe' => $eleve->classe->niveau ?? '',
            'moyenne_generale' => $moyenneGenerale,
            'rang_classe' => $rang,
            'effectif_classe' => $elevesClasse->count(),
            'dernieres_notes' => $dernieresNotes,
        ]);
    }
}