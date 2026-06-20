<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class Absence extends Model
{
    use HasFactory;

    protected $fillable = ['eleve_id', 'date', 'motif', 'justifiee'];

    protected $casts = ['justifiee' => 'boolean'];

    public function eleve(): BelongsTo
    {
        return $this->belongsTo(Eleve::class);
    }
}