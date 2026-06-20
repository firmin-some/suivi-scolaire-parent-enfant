<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
{
    Schema::create('notes', function (Blueprint $table) {
        $table->id();
        $table->foreignId('eleve_id')->constrained('eleves')->cascadeOnDelete();
        $table->foreignId('matiere_id')->constrained('matieres')->cascadeOnDelete();
        $table->string('type'); // Devoir, Composition...
        $table->decimal('valeur', 4, 2);
        $table->unsignedTinyInteger('coefficient')->default(1);
        $table->unsignedTinyInteger('trimestre'); // 1, 2 ou 3
        $table->date('date');
        $table->timestamps();
    });
}

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('notes');
    }
};
