<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Annonce;
use Illuminate\Http\Request;

class AnnonceApiController extends Controller
{
    public function index(Request $request)
    {
        $annonces = Annonce::latest('date')->get()->map(fn($a) => [
            'id' => $a->id,
            'titre' => $a->titre,
            'contenu' => $a->contenu,
            'type' => $a->type,
            'date' => $a->date->toDateString(),
        ]);

        return response()->json($annonces);
    }
}