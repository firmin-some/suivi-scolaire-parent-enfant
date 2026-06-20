<?php

namespace App\Http\Controllers;

use App\Models\Eleve;
use Illuminate\Http\Request;

class EleveController extends Controller
{
    public function index(Request $request)
    {
        $eleves = $request->user()->eleves()->get(['eleves.id', 'nom', 'prenom', 'photo_url', 'classe']);
        return response()->json($eleves);
    }

    public function show(Request $request, Eleve $eleve)
    {
        if (! $request->user()->eleves()->where('eleves.id', $eleve->id)->exists()) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $moyenneGenerale = round($eleve->notes()->avg('valeur') ?? 0, 1);
        $effectifClasse = Eleve::where('classe', $eleve->classe)->count();
        $rangClasse = 1;

        $dernieresNotes = $eleve->notes()
            ->with('matiere')
            ->latest('date')
            ->take(5)
            ->get()
            ->map(fn ($note) => [
                'matiere' => $note->matiere->nom,
                'valeur' => (float) $note->valeur,
                'date' => $note->date,
            ]);

        return response()->json([
            'id' => $eleve->id,
            'nom' => $eleve->nom,
            'prenom' => $eleve->prenom,
            'photo_url' => $eleve->photo_url,
            'classe' => $eleve->classe,
            'moyenne_generale' => $moyenneGenerale,
            'rang_classe' => $rangClasse,
            'effectif_classe' => $effectifClasse,
            'dernieres_notes' => $dernieresNotes,
        ]);
    }

    public function verify(Request $request)
    {
        $data = $request->validate([
            'nom' => ['required', 'string'],
            'classe' => ['required', 'string'],
        ]);

        $eleve = $request->user()->eleves()
            ->whereRaw('LOWER(nom) = ?', [mb_strtolower($data['nom'])])
            ->whereRaw('LOWER(classe) = ?', [mb_strtolower($data['classe'])])
            ->first(['eleves.id', 'nom', 'prenom', 'photo_url', 'classe']);

        if (! $eleve) {
            return response()->json(['message' => "Aucun enfant ne correspond à ces informations."], 404);
        }

        return response()->json($eleve);
    }
}