<?php

use App\Http\Controllers\AbsenceController;
use App\Http\Controllers\AnnonceController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\BulletinController;
use App\Http\Controllers\EleveController;
use App\Http\Controllers\NoteController;
use App\Http\Controllers\PaiementController;
use Illuminate\Support\Facades\Route;

Route::post('/login', [AuthController::class, 'login']);

Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout', [AuthController::class, 'logout']);
    Route::put('/password', [AuthController::class, 'updatePassword']);
    Route::get('/eleves', [EleveController::class, 'index']);
    Route::post('/eleves/verify', [EleveController::class, 'verify']);
    Route::get('/eleves/{eleve}', [EleveController::class, 'show']);
    Route::get('/eleves/{eleve}/notes', [NoteController::class, 'index']);
    Route::get('/eleves/{eleve}/paiements', [PaiementController::class, 'index']);
    Route::post('/eleves/{eleve}/paiements', [PaiementController::class, 'store']);
    Route::get('/eleves/{eleve}/absences', [AbsenceController::class, 'index']);
    Route::get('/eleves/{eleve}/bulletin', [BulletinController::class, 'show']);
    Route::get('/annonces', [AnnonceController::class, 'index']);
});