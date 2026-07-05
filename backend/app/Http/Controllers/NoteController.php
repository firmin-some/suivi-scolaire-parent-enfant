<?php

namespace App\Http\Controllers;

use App\Models\Note;
use App\Models\Eleve;
use App\Models\Classe;
use App\Models\Enseignant;
use Illuminate\Http\Request;
use Barryvdh\DomPDF\Facade\Pdf;

class NoteController extends Controller
{
    /**
     * Retourne la classe autorisée pour l'utilisateur connecté.
     * Gestionnaire → toutes les classes
     * Enseignant → uniquement sa classe
     */
    private function getClassesAutorisees()
    {
        $user = auth()->user();

        if ($user->hasRole('Gestionnaire')) {
            return Classe::all();
        }

        // Enseignant : uniquement sa classe
        $enseignant = Enseignant::where('user_id', $user->id)->first();
        if ($enseignant && $enseignant->classe_id) {
            return Classe::where('id', $enseignant->classe_id)->get();
        }

        return collect(); // Aucune classe si pas de profil enseignant
    }

    private function getClasseIdAutorise(): ?int
    {
        $user = auth()->user();

        if ($user->hasRole('Gestionnaire')) {
            return null; // null = toutes les classes autorisées
        }

        $enseignant = Enseignant::where('user_id', $user->id)->first();
        return $enseignant?->classe_id;
    }

    public function index()
    {
        $classes = $this->getClassesAutorisees();
        $classeAutoriseeId = $this->getClasseIdAutorise();
        return view('notes.index', compact('classes', 'classeAutoriseeId'));
    }

    public function store(Request $request)
    {
        $request->validate([
            'classe_id' => 'required|exists:classes,id',
            'trimestre' => 'required|in:T1,T2,T3',
            'notes'     => 'required|array',
        ]);

        // Vérification d'autorisation
        $classeAutoriseeId = $this->getClasseIdAutorise();
        if ($classeAutoriseeId !== null && (int)$request->classe_id !== $classeAutoriseeId) {
            return redirect()->route('notes.index')
                ->with('error', "Vous n'êtes pas autorisé à modifier les notes de cette classe.");
        }

        foreach ($request->notes as $eleveId => $matieres) {
            // Vérification supplémentaire : l'élève appartient bien à la classe
            $eleve = Eleve::find($eleveId);
            if (!$eleve || (int)$eleve->classe_id !== (int)$request->classe_id) {
                continue; // Ignore les élèves qui ne sont pas dans cette classe
            }

            foreach ($matieres as $matiere => $note) {
                if ($note === null || $note === '') continue;
                Note::updateOrCreate(
                    [
                        'eleve_id'  => $eleveId,
                        'classe_id' => $request->classe_id,
                        'trimestre' => $request->trimestre,
                        'matiere'   => $matiere,
                    ],
                    ['note' => $note]
                );
            }
        }

        return redirect()->route('notes.index')
            ->with('success', 'Notes enregistrées avec succès !');
    }

    public function getEleves(Request $request)
    {
        $classeAutoriseeId = $this->getClasseIdAutorise();

        // Si enseignant, bloque la requête pour une autre classe
        if ($classeAutoriseeId !== null && (int)$request->classe_id !== $classeAutoriseeId) {
            return response()->json(['error' => "Vous n'êtes pas autorisé à accéder à cette classe."], 403);
        }

        $eleves = Eleve::where('classe_id', $request->classe_id)
            ->with(['notes' => function($q) use ($request) {
                $q->where('trimestre', $request->trimestre);
            }])
            ->get();

        return response()->json($eleves);
    }

    public function moyennes(Request $request)
    {
        $classes  = $this->getClassesAutorisees();
        $matieres = ['Français','Mathématiques','Sciences','Histoire-Géo','Anglais','EPS'];
        $eleves   = collect();

        if ($request->filled('classe_id') && $request->filled('trimestre')) {
            // Vérification d'autorisation
            $classeAutoriseeId = $this->getClasseIdAutorise();
            if ($classeAutoriseeId !== null && (int)$request->classe_id !== $classeAutoriseeId) {
                return redirect()->route('notes.moyennes')
                    ->with('error', "Vous n'êtes pas autorisé à consulter les moyennes de cette classe.");
            }

            $eleves = Eleve::where('classe_id', $request->classe_id)
                ->with(['notes' => fn($q) =>
                    $q->where('trimestre', $request->trimestre)])
                ->get();
        }

        return view('notes.moyennes', compact('classes','matieres','eleves'));
    }

    public function classement(Request $request)
    {
        $classes = $this->getClassesAutorisees();
        $eleves  = collect();

        if ($request->filled('classe_id') && $request->filled('trimestre')) {
            $classeAutoriseeId = $this->getClasseIdAutorise();
            if ($classeAutoriseeId !== null && (int)$request->classe_id !== $classeAutoriseeId) {
                return redirect()->route('notes.classement')
                    ->with('error', "Vous n'êtes pas autorisé à consulter le classement de cette classe.");
            }

            $eleves = Eleve::where('classe_id', $request->classe_id)
                ->with(['notes' => fn($q) =>
                    $q->where('trimestre', $request->trimestre)])
                ->get()
                ->sortByDesc(fn($e) => $e->notes->avg('note') ?? 0)
                ->values();
        }

        return view('notes.classement', compact('classes','eleves'));
    }

    public function bulletinPdf(Request $request)
    {
        $request->validate([
            'classe_id' => 'required|exists:classes,id',
            'trimestre' => 'required|in:T1,T2,T3',
        ]);

        $classeAutoriseeId = $this->getClasseIdAutorise();
        if ($classeAutoriseeId !== null && (int)$request->classe_id !== $classeAutoriseeId) {
            abort(403, "Vous n'êtes pas autorisé à générer le bulletin de cette classe.");
        }

        $trimestre = $request->trimestre;
        $classe = Classe::find($request->classe_id);
        $matieres = ['Français','Mathématiques','Sciences','Histoire-Géo','Anglais','EPS'];

        $eleves = Eleve::where('classe_id', $request->classe_id)
            ->with(['notes' => function($q) use ($trimestre, $request) {
                $q->where('trimestre', $trimestre)
                  ->where('classe_id', $request->classe_id);
            }])
            ->get()
            ->sortByDesc(fn($e) => $e->notes->avg('note') ?? 0)
            ->values();

        $pdf = Pdf::loadView('pdf.bulletin', compact('classe','eleves','matieres','trimestre'))
            ->setPaper('a4', 'portrait');

        return $pdf->download('bulletin-'.$classe->nom.'-'.$trimestre.'.pdf');
    }
}