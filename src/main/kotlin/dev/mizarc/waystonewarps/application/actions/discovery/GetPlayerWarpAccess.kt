package dev.mizarc.waystonewarps.application.actions.discovery

import dev.mizarc.waystonewarps.domain.discoveries.DiscoveryRepository
import dev.mizarc.waystonewarps.domain.warps.Warp
import dev.mizarc.waystonewarps.domain.warps.WarpRepository
import java.util.UUID

class GetPlayerWarpAccess(private val discoveryRepository: DiscoveryRepository,
                          private val warpRepository: WarpRepository
) {
    fun execute(playerId: UUID): List<Warp> {
        val allWarps = warpRepository.getAll()
        val publicWarps = allWarps.filter { !it.isLocked }
        val discoveries = discoveryRepository.getByPlayer(playerId)
        val discoveredWarps = mutableListOf<Warp>()
        for (discovery in discoveries) {
            val warp = warpRepository.getById(discovery.warpId)
            if (warp != null) {
                discoveredWarps.add(warp)
            }
        }
        return (publicWarps + discoveredWarps).distinctBy { it.id }
    }
}