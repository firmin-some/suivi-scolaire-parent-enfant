<?php

namespace App\Http\Controllers;

use App\Models\Absence;
use App\Models\Eleve;
use App\Models\Notification;
use Illuminate\Http\Request;

class AbsenceController extends Controller
{
    public function index()
    {
        $classes = \App\Models\Classe::all();
        $absences = Absence::with('eleve.classe')->latest()->get();
        return view('absences.index', compact('absences', 'classes'));
    }

    public function store(Request $request)
    {
        $data = $request->validate([
            'eleve_id' => ['required', 'exists:eleves,id'],
            'date' => ['required', 'date'],
            'motif' => ['nullable', 'string', 'max:255'],
            'justifiee' => ['boolean'],
        ]);

        $data['justifiee'] = $request->has('justifiee');

        $absence = Absence::create($data);
        $eleve = Eleve::find($data['eleve_id']);

        // Notifier le parent si l'élève en a un
        if ($eleve->parent_id) {
            Notification::create([
                'eleve_id' => $eleve->id,
                'titre' => "Absence signalée — {$eleve->prenom} {$eleve->nom}",
                'message' => "Une absence a été enregistrée le {$absence->date} pour votre enfant {$eleve->prenom} {$eleve->nom}."
                    . ($absence->motif ? " Motif : {$absence->motif}." : " Aucun motif renseigné.")
                    . ($absence->justifiee ? " (Justifiée)" : " (Non justifiée)"),
                'lu' => false,
            ]);
        }

        return redirect()->route('absences.index')
            ->with('success', "Absence enregistrée et parent notifié.");
    }

    public function destroy(Absence $absence)
    {
        $absence->delete();
        return redirect()->route('absences.index')
            ->with('success', 'Absence supprimée.');
    }
}