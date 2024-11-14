class Tabuleiro {
    private val tamMaxTabuleiro = 5 // Limite de monstros no tabuleiro
    val jogadores: List<Jogador> = listOf(Jogador("Jogador 1"), Jogador("Jogador 2"))
    private val monstrosTabuleiro: MutableList<CartaMonstro> = mutableListOf()

    init {
        distribuirCartasIniciais()
    }

    private fun distribuirCartasIniciais() {
        jogadores.forEach { jogador ->
            repeat(5) { // Distribui e dá 5 cartas para cada jogador
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
        verificarMonstrosNoTabuleiro() // Verifica a quantidade de monstros no tabuleiro após a rodada
    }

    // Função para adicionar um monstro ao tabuleiro
    fun posicionarMonstro(jogador: Jogador, monstro: CartaMonstro) {
        if (monstrosTabuleiro.size < tamMaxTabuleiro) {
            monstrosTabuleiro.add(monstro)
            println("${jogador.nome} posicionou o monstro ${monstro.nome} no tabuleiro.")
        } else {
            println("Não é possível posicionar mais monstros. O limite do tabuleiro foi atingido.")
        }
    }

    // Verifica se o número máximo de monstros no tabuleiro foi atingido
    private fun verificarMonstrosNoTabuleiro() {
        if (monstrosTabuleiro.size == tamMaxTabuleiro) {
            println("O tabuleiro atingiu o limite de monstros!")
        }
    }

    // Função para limpar o tabuleiro
    fun limparTabuleiro() {
        monstrosTabuleiro.clear()
        println("O tabuleiro foi limpo.")
    }

    // Função para finalizar o jogo no tabuleiro, limpando os monstros e recursos
    fun finalizarJogo() {
        limparTabuleiro()
        println("O jogo foi finalizado. O tabuleiro foi limpo.")
    }
}
