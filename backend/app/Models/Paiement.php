<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Paiement extends Model
{
    protected $fillable = [
        'eleve_id', 'montant', 'date_paiement', 'mode_paiement', 'statut', 'recu_path'
    ];

    protected $casts = [
        'date_paiement' => 'date',
    ];

    public function eleve()
    {
        return $this->belongsTo(Eleve::class);
    }
}