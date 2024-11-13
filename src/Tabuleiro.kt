class Tabuleiro {
    private val tamMaxTabuleiro = 5 // Limite de monstros no tabuleiro
    val jogadores: List<Jogador> = listOf(Jogador("Jogador 1"), Jogador("Jogador 2"))

    init {
        distribuirCartasIniciais()
    }

    private fun distribuirCartasIniciais() {
        jogadores.forEach { jogador ->
            repeat(5) {
                val carta = gerarCartaAleatoria()
                jogador.receberCarta(carta)
            }
        }
    }

    private fun gerarCartaAleatoria(): Carta {
        val tipo = (1..2).random()
        return if (tipo == 1) {
            CartaMonstro("Monstro ${Math.random()}", "Descrição", (1000..3000).random(), (1000..3000).random())
        } else {
            CartaEquipamento("Equipamento ${Math.random()}", "Descrição", (100..500).random(), (100..500).random())
        }
    }

    fun iniciarRodada() {
        jogadores.forEach { jogador ->
            if (jogador.mao.size < 10) {
                val carta = gerarCartaAleatoria()
                jogador.receberCarta(carta)
            }
        }
        jogadores.forEach { it.resetarAtaques() }
    }
}
