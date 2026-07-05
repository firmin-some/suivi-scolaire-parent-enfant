<?php

use App\Http\Controllers\Api\AuthApiController;
use App\Http\Controllers\Api\EleveApiController;
use App\Http\Controllers\Api\NoteApiController;
use App\Http\Controllers\Api\PaiementApiController;
use App\Http\Controllers\Api\AnnonceApiController;
use App\Http\Controllers\Api\NotificationApiController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AbsenceApiController;
Route::post('/login', [AuthApiController::class, 'login']);

Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout', [AuthApiController::class, 'logout']);
    Route::put('/password', [AuthApiController::class, 'updatePassword']);
    Route::get('/eleves', [EleveApiController::class, 'index']);
    Route::post('/eleves/verify', [EleveApiController::class, 'verify']);
    Route::get('/eleves/{eleve}', [EleveApiController::class, 'show']);
    Route::get('/eleves/{eleve}/notes', [NoteApiController::class, 'index']);
    Route::get('/eleves/{eleve}/paiements', [PaiementApiController::class, 'index']);
    Route::post('/eleves/{eleve}/paiements', [PaiementApiController::class, 'store']);
    Route::get('/eleves/{eleve}/bulletin', [NoteApiController::class, 'bulletin']);
    Route::get('/annonces', [AnnonceApiController::class, 'index']);
    Route::get('/notifications', [NotificationApiController::class, 'index']);
    Route::put('/notifications/{id}/lu', [NotificationApiController::class, 'marquerLu']);
    Route::get('/eleves/{eleve}/absences', [AbsenceApiController::class, 'index']);
});