<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsToMany;
use Illuminate\Database\Eloquent\Relations\HasMany;

class Eleve extends Model
{
    use HasFactory;

    protected $fillable = ['nom', 'prenom', 'photo_url', 'classe', 'montant_total_du'];

    public function parents(): BelongsToMany
    {
        return $this->belongsToMany(User::class, 'parent_eleve');
    }

    public function notes(): HasMany
    {
        return $this->hasMany(Note::class);
    }

    public function paiements(): HasMany
    {
        return $this->hasMany(Paiement::class);
    }

    public function absences(): HasMany
    {
        return $this->hasMany(Absence::class);
    }
}