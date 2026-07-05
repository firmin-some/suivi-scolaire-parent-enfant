@extends('layouts.app')

@section('title', 'Absences')

@section('content')
<div class="container-fluid">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="fw-bold">📋 Gestion des absences</h4>
    </div>

    {{-- Formulaire de saisie --}}
    <div class="card mb-4">
        <div class="card-body">
            <h6 class="fw-bold mb-3">✏️ Signaler une absence</h6>
            <form action="{{ route('absences.store') }}" method="POST" class="row g-3">
                @csrf
                <div class="col-md-4">
                    <label class="form-label">Élève</label>
                    <select name="eleve_id" class="form-select" required>
                        <option value="">-- Choisir un élève --</option>
                        @foreach($classes as $classe)
                            <optgroup label="{{ $classe->niveau }}">
                                @foreach($classe->eleves as $eleve)
                                    <option value="{{ $eleve->id }}">{{ $eleve->nom }} {{ $eleve->prenom }}</option>
                                @endforeach
                            </optgroup>
                        @endforeach
                    </select>
                </div>
                <div class="col-md-3">
                    <label class="form-label">Date</label>
                    <input type="date" name="date" class="form-control" required value="{{ date('Y-m-d') }}">
                </div>
                <div class="col-md-3">
                    <label class="form-label">Motif (optionnel)</label>
                    <input type="text" name="motif" class="form-control" placeholder="Ex : Maladie">
                </div>
                <div class="col-md-2 d-flex align-items-end">
                    <div class="form-check me-3">
                        <input type="checkbox" name="justifiee" class="form-check-input" id="justifiee">
                        <label class="form-check-label" for="justifiee">Justifiée</label>
                    </div>
                    <button type="submit" class="btn btn-primary">Enregistrer</button>
                </div>
            </form>
        </div>
    </div>

    {{-- Liste des absences --}}
    <div class="card">
        <div class="card-body">
            <h6 class="fw-bold mb-3">📋 Historique des absences</h6>
            @if($absences->isEmpty())
                <p class="text-muted">Aucune absence enregistrée.</p>
            @else
                <table class="table table-hover">
                    <thead style="background:#1a1a2e; color:white;">
                        <tr>
                            <th>Élève</th>
                            <th>Classe</th>
                            <th>Date</th>
                            <th>Motif</th>
                            <th>Statut</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        @foreach($absences as $absence)
                        <tr>
                            <td>{{ $absence->eleve->nom }} {{ $absence->eleve->prenom }}</td>
                            <td>{{ $absence->eleve->classe->niveau ?? '-' }}</td>
                            <td>{{ \Carbon\Carbon::parse($absence->date)->format('d/m/Y') }}</td>
                            <td>{{ $absence->motif ?? 'Non renseigné' }}</td>
                            <td>
                                @if($absence->justifiee)
                                    <span class="badge bg-success">Justifiée</span>
                                @else
                                    <span class="badge bg-danger">Non justifiée</span>
                                @endif
                            </td>
                            <td>
                                <form action="{{ route('absences.destroy', $absence) }}" method="POST"
                                      onsubmit="return confirm('Supprimer cette absence ?')">
                                    @csrf @method('DELETE')
                                    <button class="btn btn-sm btn-danger">Supprimer</button>
                                </form>
                            </td>
                        </tr>
                        @endforeach
                    </tbody>
                </table>
            @endif
        </div>
    </div>
</div>
@endsection