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
h1 { color: #1B4965; border-bottom: 2px solid #1B4965; padding-bottom: 10px; margin-top: 30px; }
table { width: 100%; margin-top: 20px; border-collapse: collapse; }
td { padding: 8px; border-bottom: 1px solid #eee; }
.label { font-weight: bold; width: 200px; }
.total { font-size: 18px; font-weight: bold; color: #1B4965; margin-top: 20px; text-align: right; }
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

<h1>Reçu de paiement</h1>
<table>
<tr><td class="label">Élève</td><td>{{ $eleve->prenom }} {{ $eleve->nom }}</td></tr>
<tr><td class="label">Classe</td><td>{{ $eleve->classe }}</td></tr>
<tr><td class="label">Parent</td><td>{{ $parent->prenom }} {{ $parent->nom }}</td></tr>
<tr><td class="label">Date du versement</td><td>{{ $paiement->date }}</td></tr>
<tr><td class="label">Mode de paiement</td><td>{{ $paiement->mode_paiement }}</td></tr>
<tr><td class="label">Référence</td><td>#{{ str_pad($paiement->id, 6, '0', STR_PAD_LEFT) }}</td></tr>
</table>
<p class="total">Montant versé : {{ number_format($paiement->montant, 0, ',', ' ') }} FCFA</p>

<div class="footer">Reçu généré automatiquement — École Primaire Les Hirondelles</div>
</body>
</html>