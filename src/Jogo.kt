import kotlin.random.Random

class Jogo(private val colecaoDeCartas: List<Carta>) {
    private val jogadores: List<Jogador>
    private var rodada: Int = 0
    private var jogoFinalizado = false

    init {
        jogadores = listOf(Jogador("Jogador 1"), Jogador("Jogador 2"))
        distribuirCartasIniciais()
    }

    protected fun distribuirCartasIniciais() {
        jogadores.forEach { jogador ->
            repeat(5) {
                val carta = colecaoDeCartas.random()
                jogador.receberCarta(carta)
            }
        }
    }

    fun iniciarJogo() {
        rodada = 1
        while (jogadores.any { it.pontosDeVida > 0 } && colecaoDeCartas.isNotEmpty()) {
            println("\n------------------------------------------------------")
            println("\nRodada $rodada:")
            iniciarRodada()
            rodada++
            if (jogoFinalizado) return
        }
        verificarVencedor()
    }

    protected fun iniciarRodada() {
        jogadores.forEach { jogador ->
            if (jogoFinalizado) return

            if (jogador.mao.size < 10 && colecaoDeCartas.isNotEmpty()) {
                val novaCarta = colecaoDeCartas.random()
                jogador.receberCarta(novaCarta)
            }

            println("\n------------------------------------------------------")
            println("Bem-vindo(a) aventureiro(a) ao jogo de Cartas Monstro!!!")
            println("Escolhas do ${jogador.nome}:")
            jogador.escolherAcao(this)
        }
    }

    fun realizarAcaoJogador(jogador: Jogador, acao: String, carta: Carta? = null, alvo: CartaMonstro? = null) {
        if (jogoFinalizado) {
            println("O jogo já foi finalizado, nenhuma ação pode ser realizada.")
            return
        }
        when (acao) {
            "posicionar" -> if (carta is CartaMonstro) jogador.posicionarMonstro()
            "equipar" -> if (carta is CartaEquipamento && alvo != null) jogador.equiparMonstro()
            "descartar" -> if (carta != null) jogador.descartarCarta()
            "atacar" -> if (alvo != null && carta is CartaMonstro) jogador.realizarAtaque()
            "alterar" -> if (carta is CartaMonstro) jogador.alterarEstadoMonstro()
        }
    }


    protected fun verificarVencedor() {
        val jogadorVivo = jogadores.filter { it.pontosDeVida > 0 }
        when {
            jogadorVivo.size == 1 -> {
                println("${jogadorVivo[0].nome} venceu o jogo!")
                jogoFinalizado = true
            }

            jogadorVivo.isEmpty() -> {
                println("Empate! Ambos os jogadores perderam todos os pontos de vida.")
                jogoFinalizado = true
            }

            else -> {
                val vencedor = jogadores.maxByOrNull { it.pontosDeVida }
                println("${vencedor?.nome} venceu o jogo com ${vencedor?.pontosDeVida} pontos de vida!")
                jogoFinalizado = true
            }
        }
    }

    // Função para finalizar o jogo
    public fun finalizarJogo() {
        println("Jogo finalizado.")
        jogoFinalizado = true
    }
}
