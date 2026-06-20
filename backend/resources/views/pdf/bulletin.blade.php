<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style>
body { font-family: sans-serif; padding: 30px; color: #222; }
.header { width: 100%; margin-bottom: 10px; }
.logo { display: inline-block; width: 60px; height: 60px; border-radius: 50%; background: #1B4965; color: #fff; text-align: center; line-height: 60px; font-weight: bold; font-size: 22px; vertical-align: middle; }
.ecole-info { display: inline-block; vertical-align: middle; margin-left: 15px; }
.ecole-info h2 { margin: 0; color: #1B4965; }
.ecole-info p { margin: 0; color: #777; font-size: 12px; }
h1 { color: #1B4965; text-align: center; margin-top: 30px; }
h2.sub { color: #555; text-align: center; font-weight: normal; margin-top: -10px; }
table { width: 100%; border-collapse: collapse; margin-top: 20px; }
th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
th { background: #1B4965; color: white; }
.moyenne { font-size: 18px; font-weight: bold; text-align: right; margin-top: 20px; color: #1B4965; }
.footer { margin-top: 40px; font-size: 11px; color: #999; text-align: center; border-top: 1px solid #eee; padding-top: 10px; }
</style>
</head>
<body>
<div class="header">
  <div class="logo">EP</div>
  <div class="ecole-info">
    <h2>École Primaire Les Hirondelles</h2>
    <p>Ouagadougou, Burkina Faso</p>
  </div>
</div>

<h1>Bulletin scolaire</h1>
<h2 class="sub">{{ $eleve->prenom }} {{ $eleve->nom }} — {{ $eleve->classe }} — Trimestre {{ $trimestre }}</h2>
<table>
<tr><th>Matière</th><th>Moyenne / 20</th></tr>
@foreach($matieres as $matiere)
<tr><td>{{ $matiere['nom'] }}</td><td>{{ $matiere['moyenne'] }}</td></tr>
@endforeach
</table>
<p class="moyenne">Moyenne générale : {{ $moyenneGenerale }} / 20</p>
<div class="footer">Document généré automatiquement — École Primaire Les Hirondelles</div>
</body>
</html>