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
    Schema::create('annonces', function (Blueprint $table) {
        $table->id();
        $table->string('titre');
        $table->text('contenu');
        $table->string('type')->default('general'); // reunion, examen, paiement, general
        $table->date('date');
        $table->timestamps();
    });
}

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('annonces');
    }
};
