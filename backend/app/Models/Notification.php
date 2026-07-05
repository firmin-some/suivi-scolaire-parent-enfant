<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{
    protected $fillable = [
        'eleve_id',
        'titre',
        'message',
        'lu'
    ];

    public function eleve()
    {
        return $this->belongsTo(Eleve::class);
    }
}