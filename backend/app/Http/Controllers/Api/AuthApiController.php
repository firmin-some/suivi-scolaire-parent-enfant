<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class AuthApiController extends Controller
{
    public function login(Request $request)
    {
        $request->validate([
            'email' => ['required', 'email'],
            'password' => ['required'],
        ]);

        $user = User::where('email', $request->email)
                    ->where('role', 'parent')
                    ->first();

        if (! $user || ! Hash::check($request->password, $user->password)) {
            throw ValidationException::withMessages([
                'email' => ['Identifiants invalides.'],
            ]);
        }

        $token = $user->createToken('mobile-app')->plainTextToken;

        return response()->json([
            'token' => $token,
            'parent' => [
                'id' => $user->id,
                'nom' => $user->name,
                'prenom' => '',
                'email' => $user->email,
                'civilite' => null,
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
            'ancien_mot_de_passe' => ['required'],
            'nouveau_mot_de_passe' => ['required', 'min:6', 'confirmed'],
        ]);

        if (! Hash::check($data['ancien_mot_de_passe'], $request->user()->password)) {
            return response()->json(['message' => "L'ancien mot de passe est incorrect"], 422);
        }

        $request->user()->update(['password' => Hash::make($data['nouveau_mot_de_passe'])]);
        return response()->json(['message' => 'Mot de passe mis à jour']);
    }
}