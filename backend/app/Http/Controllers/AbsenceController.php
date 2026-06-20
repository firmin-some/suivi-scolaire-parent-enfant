<?php

namespace App\Http\Controllers;

use App\Models\Eleve;
use Illuminate\Http\Request;

class AbsenceController extends Controller
{
    public function index(Request $request, Eleve $eleve)
    {
        if (! $request->user()->eleves()->where('eleves.id', $eleve->id)->exists()) {
            return response()->json(['message' => 'Accès non autorisé'], 403);
        }

        $absences = $eleve->absences()->latest('date')->get()->map(fn($a) => [
            'id' => $a->id,
            'date' => $a->date,
            'motif' => $a->motif,
            'justifiee' => $a->justifiee,
        ]);

        return response()->json($absences);
    }
}