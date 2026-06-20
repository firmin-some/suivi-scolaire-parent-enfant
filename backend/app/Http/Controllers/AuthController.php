<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{public function login(Request $request)
{
    $credentials = $request->validate([
        'email' => ['required', 'email'],
        'password' => ['required', 'string'],
    ]);

    $user = User::where('email', $credentials['email'])->first();

    if (! $user || ! Hash::check($credentials['password'], $user->password)) {
        throw ValidationException::withMessages([
            'email' => ['Identifiants invalides'],
        ]);
    }

    $token = $user->createToken('mobile-app')->plainTextToken;

    return response()->json([
        'token' => $token,
        'parent' => [
            'id' => $user->id,
            'nom' => $user->nom,
            'prenom' => $user->prenom,
            'email' => $user->email,
            'civilite' => $user->civilite,
        ],
    ]);
}

    public function logout(Request $request)
    {
        $request->user()->currentAccessToken()->delete();

        return response()->json(['message' => 'Déconnexion réussie']);
    }

    public function updatePassword(Request $request)
    {
        $data = $request->validate([
            'ancien_mot_de_passe' => ['required', 'string'],
            'nouveau_mot_de_passe' => ['required', 'string', 'min:6', 'confirmed'],
        ]);

        $user = $request->user();

        if (! Hash::check($data['ancien_mot_de_passe'], $user->password)) {
            return response()->json(['message' => "L'ancien mot de passe est incorrect"], 422);
        }

        $user->password = Hash::make($data['nouveau_mot_de_passe']);
        $user->save();

        return response()->json(['message' => 'Mot de passe mis à jour']);
    }
}