<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Notification;
use Illuminate\Http\Request;

class NotificationApiController extends Controller
{
    public function index(Request $request)
    {
        $eleveIds = $request->user()->eleves()->pluck('id');

        $notifications = Notification::whereIn('eleve_id', $eleveIds)
            ->latest()
            ->get()
            ->map(fn($n) => [
                'id' => $n->id,
                'titre' => $n->titre,
                'message' => $n->message,
                'lu' => (bool) $n->lu,
                'date' => $n->created_at->toDateString(),
            ]);

        return response()->json($notifications);
    }

    public function marquerLu(Request $request, $id)
    {
        $eleveIds = $request->user()->eleves()->pluck('id');
        $notification = Notification::whereIn('eleve_id', $eleveIds)->findOrFail($id);
        $notification->update(['lu' => true]);
        return response()->json(['message' => 'Notification marquée comme lue']);
    }
}