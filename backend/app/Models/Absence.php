<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Absence extends Model
{
    protected $table = 'absences';

    protected $fillable = [
        'eleve_id',
        'date',
        'motif',
        'justifiee'
    ];

    protected $casts = [
        'date' => 'date',
        'justifiee' => 'boolean'
    ];

    public function eleve()
    {
        return $this->belongsTo(Eleve::class);
    }
}