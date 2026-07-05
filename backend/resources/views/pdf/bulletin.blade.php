<!DOCTYPE html>
<html lang="fr">
<head>
<meta charset="UTF-8">
<style>
body { font-family: DejaVu Sans, sans-serif; padding: 30px; font-size: 12px; color: #222; }

/* En-tête école */
.header { width: 100%; margin-bottom: 20px; border-bottom: 2px solid #1B4965; padding-bottom: 15px; }
.logo-cercle { display: inline-block; width: 55px; height: 55px; border-radius: 50%; background: #1B4965; color: #F4A300; text-align: center; line-height: 55px; font-weight: bold; font-size: 18px; vertical-align: middle; }
.ecole-info { display: inline-block; vertical-align: middle; margin-left: 12px; }
.ecole-info h2 { margin: 0; color: #1B4965; font-size: 16px; }
.ecole-info p { margin: 0; color: #777; font-size: 11px; }

/* Titre */
h1 { text-align: center; color: #1B4965; font-size: 18px; margin: 20px 0 5px; }
.sous-titre { text-align: center; color: #555; font-size: 13px; margin-bottom: 20px; }

/* Infos élève */
.infos-eleve { background: #f4f7f9; border: 1px solid #ddd; border-radius: 6px; padding: 10px 15px; margin-bottom: 20px; }
.infos-eleve table { width: 100%; border: none; margin: 0; }
.infos-eleve td { border: none; padding: 3px 8px; font-size: 12px; }

/* Tableau notes */
table.notes { width: 100%; border-collapse: collapse; margin-top: 10px; }
table.notes th { background: #1B4965; color: white; padding: 8px; text-align: left; }
table.notes td { border: 1px solid #ccc; padding: 8px; }
table.notes tr:nth-child(even) { background: #f9f9f9; }

/* Moyenne et mention */
.moyenne { font-size: 14px; font-weight: bold; text-align: right; margin-top: 15px; color: #1B4965; }
.mention { text-align: right; margin-top: 5px; font-size: 13px; }
.mention span { padding: 4px 12px; border-radius: 4px; color: white; }
.mention-tb { background: #1a7a1a; }
.mention-bien { background: #2196F3; }
.mention-ab { background: #FF9800; }
.mention-pass { background: #9E9E9E; }
.mention-fail { background: #e53935; }

/* Pied de page */
.footer { margin-top: 40px; font-size: 10px; color: #999; text-align: center; border-top: 1px solid #eee; padding-top: 10px; }
</style>
</head>
<body>

{{-- En-tête école --}}
<div class="header">
    <div class="logo-cercle">EP</div>
    <div class="ecole-info">
        <h2>École Primaire Les Hirondelles</h2>
        <p>Ouagadougou, Burkina Faso</p>
    </div>
</div>

{{-- Titre --}}
<h1>Bulletin de Notes</h1>
<p class="sous-titre">{{ $trimestre }}er Trimestre &mdash; Année scolaire {{ date('Y') }}</p>

{{-- Infos élève --}}
<div class="infos-eleve">
    <table>
        <tr>
            <td><strong>Élève :</strong> {{ $eleve->prenom }} {{ $eleve->nom }}</td>
            <td><strong>Classe :</strong> {{ $classe->niveau ?? $classe->nom ?? '' }}</td>
        </tr>
        <tr>
            <td><strong>Parent :</strong> {{ $eleve->nom_parent ?? ($eleve->parent->name ?? '') }}</td>
            <td><strong>Date de génération :</strong> {{ now()->format('d/m/Y à H:i') }}</td>
        </tr>
    </table>
</div>

{{-- Tableau des notes --}}
<table class="notes">
    <thead>
        <tr>
            <th>Matière</th>
            <th>Moyenne / 20</th>
        </tr>
    </thead>
    <tbody>
        @foreach($matieres as $matiere)
        <tr>
            <td>{{ $matiere['nom'] }}</td>
            <td>{{ number_format($matiere['moyenne'], 2) }}</td>
        </tr>
        @endforeach
    </tbody>
</table>

{{-- Moyenne générale --}}
@php
    $moy = (float) $moyenneGenerale;
    if ($moy >= 16) { $mention = 'Très bien'; $classe_mention = 'mention-tb'; }
    elseif ($moy >= 14) { $mention = 'Bien'; $classe_mention = 'mention-bien'; }
    elseif ($moy >= 12) { $mention = 'Assez bien'; $classe_mention = 'mention-ab'; }
    elseif ($moy >= 10) { $mention = 'Passable'; $classe_mention = 'mention-pass'; }
    else { $mention = 'Insuffisant'; $classe_mention = 'mention-fail'; }
@endphp

<p class="moyenne">Moyenne générale : {{ number_format($moyenneGenerale, 2) }} / 20</p>
<p class="mention">Mention : <span class="{{ $classe_mention }}">{{ $mention }}</span></p>

<div class="footer">Bulletin généré automatiquement — École Primaire Les Hirondelles</div>

</body>
</html>