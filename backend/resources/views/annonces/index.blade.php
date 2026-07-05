@extends('layouts.app')

@section('content')
<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>📢 Annonces</h2>
        <a href="{{ route('annonces.create') }}" class="btn btn-primary">+ Nouvelle annonce</a>
    </div>

    @if(session('success'))
        <div class="alert alert-success">{{ session('success') }}</div>
    @endif

    @if($annonces->isEmpty())
        <p>Aucune annonce pour l'instant.</p>
    @else
        <table class="table table-striped">
            <thead>
                <tr><th>Titre</th><th>Type</th><th>Date</th><th>Action</th></tr>
            </thead>
            <tbody>
                @foreach($annonces as $annonce)
                <tr>
                    <td>{{ $annonce->titre }}</td>
                    <td><span class="badge bg-info">{{ $annonce->type }}</span></td>
                    <td>{{ $annonce->date->format('d/m/Y') }}</td>
                    <td>
                        <form action="{{ route('annonces.destroy', $annonce) }}" method="POST" onsubmit="return confirm('Supprimer cette annonce ?')">
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
@endsection