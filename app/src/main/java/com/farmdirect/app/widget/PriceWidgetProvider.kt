package com.farmdirect.app.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.farmdirect.app.MainActivity
import com.farmdirect.app.R

class PriceWidgetProvider : AppWidgetProvider() {
    
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }
    
    private fun updateWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_prices)
        
        // Set current prices
        views.setTextViewText(R.id.tv_widget_title, "🌾 Today's Prices")
        views.setTextViewText(R.id.tv_maize_price, "Maize: KES 3,500/bag")
        views.setTextViewText(R.id.tv_beans_price, "Beans: KES 8,000/bag")
        views.setTextViewText(R.id.tv_potatoes_price, "Potatoes: KES 2,500/bag")
        
        // Set click intent
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)
        
        manager.updateAppWidget(widgetId, views)
    }
}
