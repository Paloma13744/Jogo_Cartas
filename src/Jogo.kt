import kotlin.random.Random

class Jogo(private val colecaoDeCartas: List<Carta>) {
    private val jogadores: List<Jogador>
    private var rodada: Int = 0
    private var jogoFinalizado = false

    init {  // Inicializa o jogo com 2 jogadores e distrbui as cartas
        jogadores = listOf(Jogador("Jogador 1"), Jogador("Jogador 2"))
        distribuirCartasIniciais()
    }

    protected fun distribuirCartasIniciais() {  // Função responsável por distribuir as cartas iniciais para cada jogador.
        jogadores.forEach { jogador ->
            repeat(5) {
                val carta = colecaoDeCartas.random()
                jogador.receberCarta(carta)
            }
        }
    }

    fun iniciarJogo() {
        jogadores.forEach { it.equipamentoUsado = false }
        rodada = 1
        while (jogadores.any { it.pontosDeVida > 0 } && colecaoDeCartas.isNotEmpty()) {
            println("\n------------------------------------------------------")
            println("\nRodada $rodada:")
            iniciarRodada()
            rodada++
            if (jogoFinalizado) return // Se o jogo terminar, sai da função.
        }
        verificarVencedor()
    }

    protected fun iniciarRodada() {  // Função responsável por gerenciar o início de cada rodada.
        jogadores.forEach { it.equipamentoUsado = false }
        jogadores.forEach { jogador ->
            if (jogoFinalizado) return

            if (jogador.mao.size < 10 && colecaoDeCartas.isNotEmpty()) {
                val novaCarta = colecaoDeCartas.random()
                jogador.receberCarta(novaCarta)
            }

            println("\n------------------------------------------------------")
            if(rodada ==1) {
                println("Bem-vindo(a) aventureiro(a) ao jogo de Cartas Monstro!!!")
            }
            println("Escolhas do ${jogador.nome}:")
            jogador.escolherAcao(this) // Chama a função onde o jogador escolhe sua ação.


        }
    }

    fun realizarAcaoJogador(jogador: Jogador, acao: String, carta: Carta? = null, alvo: CartaMonstro? = null) {
        if (jogoFinalizado) {
            println("O jogo já foi finalizado, nenhuma ação pode ser realizada.")
            return
        }
        when (acao) {
            "posicionar" -> if (carta is CartaMonstro) jogador.posicionarMonstro()
            "equipar" -> if (carta is CartaEquipamento && alvo != null) {
                if (jogador.equipamentoUsado) {
                    println("${jogador.nome} já usou um equipamento nesta rodada!")
                } else {
                    jogador.equiparMonstro()
                    jogador.equipamentoUsado = true // Marca o uso de equipamento
                }
            }

            "descartar" -> if (carta != null) jogador.descartarCarta()
            "atacar" -> if (alvo != null && carta is CartaMonstro) {
                val oponente = obterOponente(jogador)  // Usando a função para obter oponente
                oponente?.let {
                    jogador.realizarAtaque(it)  // Ataque no oponente
                }
            }

            "alterar" -> if (carta is CartaMonstro) jogador.alterarEstadoMonstro()
        }
    }

    // Função para obter o oponente de um jogador
    fun obterOponente(jogador: Jogador): Jogador? {
        return if (jogador == jogadores[0]) jogadores[1] else jogadores[0]
    }

    fun verificarVencedor() {
        val jogadorVivo = jogadores.filter { it.pontosDeVida > 0 }
        when {
            jogadorVivo.size == 1 -> {
                println("${jogadorVivo[0].nome} venceu o jogo!")
            }

            jogadorVivo.isEmpty() -> {
                println("Empate! Ambos os jogadores perderam todos os pontos de vida.")
            }

            else -> {
                val vencedor = jogadores.maxByOrNull { it.pontosDeVida }
                println("${vencedor?.nome} venceu o jogo com ${vencedor?.pontosDeVida} pontos de vida!")
            }
        }
        jogoFinalizado = true // Marca o jogo como finalizado.

    }

    // Função para finalizar o jogo
    public fun finalizarJogo() {
        println("Jogo finalizado.")
        jogoFinalizado = true
    }
}