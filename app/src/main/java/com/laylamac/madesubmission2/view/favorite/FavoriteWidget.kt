package com.laylamac.madesubmission2.view.favorite

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import com.laylamac.madesubmission2.R
import com.laylamac.madesubmission2.utils.StackWidgetService

class FavoriteWidget : AppWidgetProvider() {

    private val TOAST_ACTION = "toast_action"
    val EXTRA_ITEM = "extra_item"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val views = RemoteViews(context.packageName, R.layout.favorite_widget)
            views.setRemoteAdapter(R.id.stack_view_favorite, intent)
            views.setEmptyView(R.id.stack_view_favorite, R.id.tv_empty_favorite)
            val toast = Intent(context, FavoriteWidget::class.java)
            toast.action = FavoriteWidget().TOAST_ACTION
            toast.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val toastPending =
                PendingIntent.getBroadcast(context, 0, toast, PendingIntent.FLAG_CANCEL_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view_favorite, toastPending)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewPosition = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Movie position $viewPosition", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
