<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use App\Models\Eleve;
use App\Models\User;
use Illuminate\Auth\Events\Registered;
use Illuminate\Http\RedirectResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\Rules;
use Illuminate\View\View;

class RegisteredUserController extends Controller
{
    public function create(): View
    {
        return view('auth.register');
    }

    public function store(Request $request): RedirectResponse
    {
        $request->validate([
            'name'     => ['required', 'string', 'max:255'],
            'email'    => ['required', 'string', 'lowercase', 'email', 'max:255', 'unique:'.User::class],
            'password' => ['required', 'confirmed', Rules\Password::defaults()],
        ]);

        $user = User::create([
            'name'     => $request->name,
            'email'    => $request->email,
            'password' => Hash::make($request->password),
            'role'     => 'parent',
        ]);

        $user->assignRole('Parent');

        // Liaison automatique avec les élèves dont le nom_parent correspond
        $elevesLies = Eleve::whereRaw('LOWER(nom_parent) = ?', [mb_strtolower(trim($request->name))])
            ->whereNull('parent_id')
            ->get();

        foreach ($elevesLies as $eleve) {
            $eleve->update(['parent_id' => $user->id]);
        }

        event(new Registered($user));
        Auth::login($user);

        if ($elevesLies->isEmpty()) {
            return redirect()->route('parent.dashboard')
                ->with('warning', "Votre compte a été créé mais aucun enfant n'a été trouvé avec ce nom. Contactez l'administration pour faire le lien.");
        }

        return redirect()->route('parent.dashboard')
            ->with('success', "Compte créé avec succès. {$elevesLies->count()} enfant(s) lié(s) à votre compte.");
    }
}