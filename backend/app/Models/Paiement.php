<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Paiement extends Model
{
    use HasFactory;

    protected $fillable = ['eleve_id', 'montant', 'date', 'mode_paiement', 'recu_path'];

    public function eleve(): BelongsTo
    {
        return $this->belongsTo(Eleve::class);
    }
}