open class Jogador(private var nome: String) {
    protected var pontosdeVida = 10000 // Pontos iniciais de um jogador
    protected val mao = mutableListOf<Carta>() // Cartas do jogador
    protected val tabuleiro = Tabuleiro() // Usando a nova classe Tabuleiro

    fun mostrarMao() {
        println("Mão de $nome:")
        mao.forEachIndexed { index, carta ->
            println("Carta $index: ${carta.obterNome()} - Tipo: ${carta.obterTipo()}")
        }
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
        if (mao[indice] is CartaMonstro) {
            val monstro = mao.removeAt(indice) as CartaMonstro
            if (!tabuleiro.adicionarMonstro(monstro)) {
                // Caso o monstro não seja adicionado (tabuleiro cheio)
                mao.add(monstro) // Volta o monstro para a mão
            }
        } else {
            println("Carta selecionada não é um monstro.")
        }
    }

    // Equipar um monstro com uma carta de equipamento
    fun equiparMonstro(indiceMonstro: Int, indiceEquipamento: Int) {
        if (indiceMonstro in 0 until tabuleiro.getMonstros().size && indiceEquipamento in 0 until mao.size && mao[indiceEquipamento] is CartaEquipamento) {
            val monstro = tabuleiro.getMonstros()[indiceMonstro]
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
        tabuleiro.realizarAtaque(monstroAtacante, monstroOponente)
    }

    // Realizar um ataque de um monstro contra outro monstro do adversário
    fun atacarComMonstro(indiceAtacante: Int, indiceOponente: Int) {
        tabuleiro.atacarComMonstro(indiceAtacante, indiceOponente)
    }

    // Metodo para alterar o estado de um monstro (ataque/defesa)
    fun alterarEstadoMonstro(indice: Int) {
        tabuleiro.alterarEstadoMonstro(indice)
    }

    fun perderPontosDeVida(dano: Int) {
        pontosdeVida = (pontosdeVida - dano).coerceAtLeast(0) // Garante que não fique negativo
        println("$nome perdeu $dano pontos de vida! Pontos de vida restantes: $pontosdeVida")
    }

    // Retorna o nome do jogador
    fun getNome(): String = nome

    // Getter para os pontos de vida
    fun getPontosVida(): Int = pontosdeVida

    // Setter para os pontos de vida
    fun setPontosVida(novoValor: Int) {
        if (novoValor >= 0) {
            pontosdeVida = novoValor
        }
    }

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
                if (tabuleiro.isVazio()) {
                    println("Não há monstros no tabuleiro.")
                } else {
                    mostrarMao()
                    println("Escolha o índice do monstro no tabuleiro:")
                    val indiceMonstro = readLine()?.toIntOrNull() ?: -1
                    println("Escolha o índice da carta de equipamento:")
                    val indiceEquipamento = readLine()?.toIntOrNull() ?: -1
                    equiparMonstro(indiceMonstro, indiceEquipamento)
                }
            }

            3 -> {
                mostrarMao()
                println("Escolha o índice da carta para descartar:")
                val indice = readLine()?.toIntOrNull() ?: -1
                descartarCarta(indice)
            }

            4 -> {
                if (tabuleiro.isVazio()) {
                    println("Não há monstros no tabuleiro para atacar.")
                } else {
                    println("Escolha o índice do monstro atacante:")
                    val indiceAtacante = readLine()?.toIntOrNull() ?: -1
                    println("Escolha o índice do monstro oponente:")
                    val indiceOponente = readLine()?.toIntOrNull() ?: -1
                    atacarComMonstro(indiceAtacante, indiceOponente)
                }
            }

            5 -> {
                if (tabuleiro.isVazio()) {
                    println("Não há monstros no tabuleiro.")
                } else {
                    println("Escolha o índice do monstro para alterar o estado:")
                    val indice = readLine()?.toIntOrNull() ?: -1
                    alterarEstadoMonstro(indice)
                }
            }

            6 -> println("Saindo do menu.")
            else -> println("Opção inválida.")
        }
    }





}
