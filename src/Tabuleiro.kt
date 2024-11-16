class Tabuleiro {
    private val tamMaxTabuleiro = 5 // Limite de monstros no tabuleiro
    val jogadores: List<Jogador> = listOf(Jogador("Jogador 1"), Jogador("Jogador 2"))
    private val monstrosTabuleiro: MutableList<CartaMonstro> = mutableListOf()

    init {
        distribuirCartasIniciais()  // Distribui as cartas
    }

    private fun distribuirCartasIniciais() {
        jogadores.forEach { jogador ->
            repeat(5) { // Distribui e dá 5 cartas aleatórias para cada jogador
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
        jogadores.forEach { jogador ->
            jogador.tabuleiro.filterIsInstance<CartaMonstro>().forEach {
                it.jaAtacou = false
            }
        }
        verificarMonstrosNoTabuleiro() // Verifica a quantidade de monstros no tabuleiro após a rodada

    }

    // Verifica se o número máximo de monstros no tabuleiro foi atingido
    fun verificarMonstrosNoTabuleiro() {
        if (monstrosTabuleiro.size == tamMaxTabuleiro) {
            println("O tabuleiro atingiu o limite de monstros!")
        }
    }

    // Função para limpar o tabuleiro
    fun limparTabuleiro() {
        monstrosTabuleiro.clear()
        println("O tabuleiro foi limpo.")
    }

    // Função para finalizar o jogo no tabuleiro
    fun finalizarJogo() {
        limparTabuleiro()
        println("O jogo foi finalizado. O tabuleiro foi limpo.")
    }


}
