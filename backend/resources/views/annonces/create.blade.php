@extends('layouts.app')

@section('content')
<div class="container" style="max-width:600px">
    <h2>📢 Nouvelle annonce</h2>
    <form action="{{ route('annonces.store') }}" method="POST">
        @csrf
        <div class="mb-3">
            <label class="form-label">Titre</label>
            <input type="text" name="titre" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Type</label>
            <select name="type" class="form-select" required>
                <option value="general">Général</option>
                <option value="reunion">Réunion</option>
                <option value="examen">Examen</option>
                <option value="paiement">Paiement</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Date</label>
            <input type="date" name="date" class="form-control" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Contenu</label>
            <textarea name="contenu" class="form-control" rows="5" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary w-100">Publier l'annonce</button>
    </form>
</div>
@endsection