<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Eleve;
use Illuminate\Http\Request;

class AbsenceApiController extends Controller
{
    public function index(Request $request, Eleve $eleve)
    {
        if ($eleve->parent_id !== $request->user()->id) {
            return response()->json([
                'message' => 'Accès non autorisé'
            ], 403);
        }

        return response()->json(
            $eleve->absences()
                  ->orderBy('date', 'desc')
                  ->get()
        );
    }
}