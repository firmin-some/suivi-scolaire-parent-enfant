<?php

namespace App\Http\Controllers;

use App\Models\Annonce;
use App\Models\Eleve;
use App\Models\Notification;
use Illuminate\Http\Request;

class AnnonceController extends Controller
{
    public function index()
    {
        $annonces = Annonce::latest()->get();
        return view('annonces.index', compact('annonces'));
    }

    public function create()
    {
        return view('annonces.create');
    }

    public function store(Request $request)
    {
        $data = $request->validate([
            'titre' => ['required', 'string', 'max:255'],
            'contenu' => ['required', 'string'],
            'type' => ['required', 'in:reunion,examen,paiement,general'],
            'date' => ['required', 'date'],
        ]);

        $annonce = Annonce::create($data);

        // Créer une notification pour chaque élève
        $eleves = Eleve::all();
        foreach ($eleves as $eleve) {
            Notification::create([
                'eleve_id' => $eleve->id,
                'titre' => $annonce->titre,
                'message' => $annonce->contenu,
                'lu' => false,
            ]);
        }

        return redirect()->route('annonces.index')->with('success', 'Annonce publiée et notifications envoyées.');
    }

    public function destroy(Annonce $annonce)
    {
        $annonce->delete();
        return redirect()->route('annonces.index')->with('success', 'Annonce supprimée.');
    }
}