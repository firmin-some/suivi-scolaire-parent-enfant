<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Note extends Model
{
    use HasFactory;

    protected $fillable = ['eleve_id', 'matiere_id', 'type', 'valeur', 'coefficient', 'trimestre', 'date'];

    public function eleve(): BelongsTo
    {
        return $this->belongsTo(Eleve::class);
    }

    public function matiere(): BelongsTo
    {
        return $this->belongsTo(Matiere::class);
    }
}