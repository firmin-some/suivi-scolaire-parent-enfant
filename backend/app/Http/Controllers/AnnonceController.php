<?php

namespace App\Http\Controllers;

use App\Models\Annonce;
use Illuminate\Http\Request;

class AnnonceController extends Controller
{
    public function index(Request $request)
    {
        $annonces = Annonce::latest('date')->get()->map(fn($a) => [
            'id' => $a->id,
            'titre' => $a->titre,
            'contenu' => $a->contenu,
            'type' => $a->type,
            'date' => $a->date,
        ]);

        return response()->json($annonces);
    }
}