package com.farmdirect.app.ui.screens.referral

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*
import com.farmdirect.app.util.ReferralManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReferralScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val referralManager = remember { ReferralManager(context) }
    val stats by referralManager.referralStats.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Invite Friends", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Section
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🤝", fontSize = 64.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("Invite Farmers, Earn Rewards", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                    Text("You and your friend both earn KES 100 when they complete their first sale!", color = TextSecondary, textAlign = TextAlign.Center)
                }
            }
            
            Spacer(Modifier.height(20.dp))
            
            // Referral Code
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Your Referral Code", color = TextWhite.copy(alpha = 0.8f), fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        stats.referralCode,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp,
                        letterSpacing = 6.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { referralManager.shareReferral(context) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGold)
                    ) {
                        Icon(Icons.Default.Share, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Share Your Link", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Stats
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("👥", "${stats.totalReferrals}", "Total\nInvited", Modifier.weight(1f))
                StatCard("✅", "${stats.successfulReferrals}", "Successful", Modifier.weight(1f))
                StatCard("💰", "KES ${"%,.0f".format(stats.totalEarned)}", "Earned", Modifier.weight(1f))
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Rank
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Your Rank", color = TextSecondary, fontSize = 13.sp)
                    Text(stats.rank, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text("Refer ${5 - (stats.successfulReferrals % 5)} more to reach next rank!", color = PrimaryGreen, fontSize = 13.sp)
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Reward Tiers
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("How You Earn", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    RewardRow("📝", "Friend signs up", "KES 50", stats.totalReferrals > 0)
                    RewardRow("💰", "Friend completes first sale", "KES 100", stats.successfulReferrals > 0)
                    RewardRow("🛒", "Friend makes first purchase", "KES 100", stats.successfulReferrals > 0)
                    RewardRow("⭐", "Friend subscribes to Premium", "KES 200", stats.successfulReferrals > 0)
                }
            }
        }
    }
}

@Composable
fun StatCard(emoji: String, value: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 28.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryGreen)
            Text(label, color = TextSecondary, fontSize = 11.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun RewardRow(emoji: String, action: String, reward: String, earned: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(emoji, fontSize = 24.sp)
        Spacer(Modifier.width(12.dp))
        Text(action, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
        Text(reward, fontWeight = FontWeight.Bold, color = if (earned) StatusSuccess else PrimaryGreen)
        if (earned) {
            Icon(Icons.Default.CheckCircle, null, tint = StatusSuccess, modifier = Modifier.size(20.dp))
        }
    }
}
