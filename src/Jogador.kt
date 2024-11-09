open class Jogador(private var nome: String) {
    protected var pontosdeVida = 10000 // Pontos iniciais de um jogador
    protected val mao = mutableListOf<Carta>() // Cartas do jogador
    protected val tabuleiro = mutableListOf<CartaMonstro>() // Lista de monstros no tabuleiro
    protected val tamMaxTabuleiro = 5 // Limite de monstros no tabuleiro

    // Mostrar cartas na mão
    fun mostrarMao() {
        println("Mão de $nome:")
        mao.forEachIndexed { index, carta -> println("Carta $index: ${carta.obterNome()}") }
    }

    // Adiciona uma carta à mão do jogador
    fun adicionarCarta(carta: Carta) {
        if (mao.size < 10) {
            mao.add(carta)
        } else {
            println("Mão está cheia, não é possível adicionar mais cartas.")
        }
    }

    // Coloca um monstro da mão no tabuleiro
    fun colocarMonstroNoTabuleiro(indice: Int) {
        if (tabuleiro.size < tamMaxTabuleiro && mao[indice] is CartaMonstro) {
            val monstro = mao.removeAt(indice) as CartaMonstro
            tabuleiro.add(monstro)
            println("${monstro.obterNome()} foi colocado no tabuleiro.")
        } else {
            println("Não é possível adicionar mais monstros no tabuleiro.")
        }
    }

    // Equipar um monstro com uma carta de equipamento
    fun equiparMonstro(indiceMonstro: Int, indiceEquipamento: Int) {
        if (indiceMonstro in 0 until tabuleiro.size && indiceEquipamento in 0 until mao.size && mao[indiceEquipamento] is CartaEquipamento) {
            val monstro = tabuleiro[indiceMonstro]
            val equipamento = mao.removeAt(indiceEquipamento) as CartaEquipamento
            monstro.equiparCarta(equipamento)
            println("${monstro.obterNome()} foi equipado com ${equipamento.obterNome()}.")
        } else {
            println("Equipamento ou monstro inválidos.")
        }
    }

    // Descartar uma carta da mão
    fun descartarCarta(indice: Int) {
        if (indice in 0 until mao.size) {
            val cartaDescartada = mao.removeAt(indice)
            println("${cartaDescartada.obterNome()} foi descartada.")
        } else {
            println("Índice inválido para descartar carta.")
        }
    }

    // Realizar um ataque contra o oponente
    fun realizarAtaque(monstroAtacante: CartaMonstro, monstroOponente: CartaMonstro) {
        val dano = monstroAtacante.poder() - monstroOponente.poder()
        if (dano > 0) {
            println("${monstroAtacante.obterNome()} causou $dano pontos de dano em ${monstroOponente.obterNome()}.")
            monstroOponente.reduzirVida(dano)
        } else {
            println("${monstroOponente.obterNome()} se defendeu do ataque.")
        }
    }

    // Realizar um ataque de um monstro contra outro monstro do adversário
    fun atacarComMonstro(indiceAtacante: Int, indiceOponente: Int) {
        if (indiceAtacante in 0 until tabuleiro.size && indiceOponente in 0 until tabuleiro.size) {
            val monstroAtacante = tabuleiro[indiceAtacante]
            val monstroOponente = tabuleiro[indiceOponente]
            realizarAtaque(monstroAtacante, monstroOponente)
        } else {
            println("Índices inválidos para ataque.")
        }
    }

    // Metodo para alterar o estado de um monstro (ataque/defesa)
    fun alterarEstadoMonstro(indice: Int) {
        if (indice in 0 until tabuleiro.size) {
            val monstro = tabuleiro[indice]
            monstro.alterarEstado()
            println("${monstro.obterNome()} agora está em estado ${monstro.modoAtaque()}")
        } else {
            println("Índice inválido para alterar o estado do monstro.")
        }
    }

    // Metodo para mostrar o menu e permitir que o jogador escolha a ação
    fun executarAcao() {
        println("\nEscolha uma ação durante sua rodada:")
        println("1 - Posicionar um novo monstro no tabuleiro")
        println("2 - Equipar um monstro com uma carta de equipamento")
        println("3 - Descartar uma carta da mão")
        println("4 - Realizar um ataque contra o oponente")
        println("5 - Alterar o estado de um monstro (ataque/defesa)")
        println("6 - Sair")

        when (readLine()?.toIntOrNull()) {
            1 -> {
                mostrarMao()
                println("Escolha o índice da carta para colocar no tabuleiro:")
                val indice = readLine()?.toIntOrNull() ?: -1
                colocarMonstroNoTabuleiro(indice)
            }

            2 -> {
                if (tabuleiro.isNotEmpty()) {
                    mostrarMao()
                    println("Escolha o índice do monstro no tabuleiro:")
                    val indiceMonstro = readLine()?.toIntOrNull() ?: -1
                    println("Escolha o índice da carta de equipamento:")
                    val indiceEquipamento = readLine()?.toIntOrNull() ?: -1
                    equiparMonstro(indiceMonstro, indiceEquipamento)
                } else {
                    println("Não há monstros no tabuleiro.")
                }
            }

            3 -> {
                mostrarMao()
                println("Escolha o índice da carta para descartar:")
                val indice = readLine()?.toIntOrNull() ?: -1
                descartarCarta(indice)
            }

            4 -> {
                if (tabuleiro.size < 2) {
                    println("Não há monstros suficientes para atacar.")
                    return
                }
                println("Escolha o índice do monstro atacante:")
                val indiceAtacante = readLine()?.toIntOrNull() ?: -1
                println("Escolha o índice do monstro oponente:")
                val indiceOponente = readLine()?.toIntOrNull() ?: -1
                atacarComMonstro(indiceAtacante, indiceOponente)
            }

            5 -> {
                if (tabuleiro.isNotEmpty()) {
                    println("Escolha o índice do monstro para alterar o estado:")
                    val indice = readLine()?.toIntOrNull() ?: -1
                    alterarEstadoMonstro(indice)
                } else {
                    println("Não há monstros no tabuleiro.")
                }
            }

            6 -> println("Saindo do menu.")
            else -> println("Opção inválida.")
        }
    }

    // Retorna o nome do jogador
    fun getNome(): String = nome

    // Getter para os pontos de vida
    fun getPontosVida(): Int = pontosdeVida

    // Setter para os pontos de vida
    fun setPontosVida(novoValor: Int) {
        if (novoValor >= 0) {
            pontosdeVida = novoValor
        } else {
            pontosdeVida = 0 // Impede que pontos de vida sejam negativos
        }
    }


    // Verifica se o jogador ainda tem monstros no tabuleiro
    fun temMonstrosNoTabuleiro(): Boolean {
        return tabuleiro.isNotEmpty()
    }

    // Retorna os monstros do jogador
    fun getMonstrosNoTabuleiro(): List<CartaMonstro> {
        return tabuleiro
    }

    // Ganho de pontos de vida do monstro
    fun ganharPontosDeVida(pontos: Int) {
        pontosdeVida += pontos
        println("$nome ganhou $pontos pontos de vida. Pontos atuais: $pontosdeVida")
    }

    // Perca de pontos de vida do monstro
    fun perderPontosDeVida(pontos: Int) {
        pontosdeVida -= pontos
        if (pontosdeVida < 0) pontosdeVida = 0 // Impede que os pontos de vida fiquem negativos
        println("$nome perdeu $pontos pontos de vida. Pontos atuais: $pontosdeVida")
    }
}
