package jp.cha84rakanal.simplebuttonwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class SimpleButtonWidget : AppWidgetProvider() {

    companion object {
        var count = 0;
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action.equals("CLICK_TEST")) {
            Log.e("Log Widget","widget_action")
            buttonClick(context,intent!!.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,0))
        }
    }

    private fun buttonClick(context: Context?, appWidgetId: Int) {
        val views = RemoteViews(context?.packageName, R.layout.simple_button_widget)
        views.setTextViewText(R.id.appwidget_btn, ""+ ++count)

        val manager = AppWidgetManager.getInstance(context)
        val widget = ComponentName(context!!, SimpleButtonWidget::class.java)
        manager.updateAppWidget(widget, views)
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.simple_button_widget)
    views.setTextViewText(R.id.appwidget_btn, widgetText)

    // 明示的インテントレシーバー
    val intent = Intent(context,SimpleButtonWidget::class.java)
    intent.action = "CLICK_TEST"
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    val pendingIntent1 = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.appwidget_btn, pendingIntent1)
    Log.e("Log Widget","updateAppWidget")

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}