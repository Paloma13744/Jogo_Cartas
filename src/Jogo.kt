class Jogo(val jogador1: Jogador, val jogador2: Jogador, val baralho: MutableList<Carta>) {

    // Iniciar o jogo
    fun iniciar() {
        distribuirCartasIniciais(jogador1)
        distribuirCartasIniciais(jogador2)

        var turno = 1
        while (jogador1.getPontosVida() > 0 && jogador2.getPontosVida() > 0) {  // Usando getter para acessar os pontos de vida
            println("Turno $turno")
            turnoDoJogador(jogador1)
            if (jogador2.getPontosVida() <= 0) break  // Verifica se jogador 2 perdeu
            turnoDoJogador(jogador2)
            if (jogador1.getPontosVida() <= 0) break  // Verifica se jogador 1 perdeu
            turno++
        }
        verificarVencedor()
    }
    private fun distribuirCartasIniciais(jogador: Jogador) {
        repeat(5) {
            if (baralho.isNotEmpty()) {
                jogador.adicionarCarta(baralho.removeAt(0))
            } else {
                println("O baralho acabou antes de distribuir todas as cartas.")
            }
        }
    }

    // Turno de um jogador
    private fun turnoDoJogador(jogador: Jogador) {
        println("Turno de ${jogador.getNome()}")
        jogador.executarAcao()
        println("${jogador1.getNome()} - Vida: ${jogador1.getPontosVida()}")
        println("${jogador2.getNome()} - Vida: ${jogador2.getPontosVida()}")
    }

    private fun atacarJogador(atacante: Jogador, defensor: Jogador) {
        val dano = 10 // Pode ajustar com base em cartas ou habilidades
        println("${atacante.getNome()} ataca ${defensor.getNome()} causando $dano de dano!")
        defensor.perderPontosDeVida(dano)
    }

    // Verificar o vencedor
    private fun verificarVencedor() {
        when {
            jogador1.getPontosVida() <= 0 -> println("${jogador2.getNome()} venceu!")
            jogador2.getPontosVida() <= 0 -> println("${jogador1.getNome()} venceu!")
            else -> println("Empate!")
        }
    }
}
