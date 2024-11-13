class Jogador(val nome: String) {
    val mao = mutableListOf<Carta>()
    var pontosDeVida = 1000
    val tabuleiro = mutableListOf<CartaMonstro>()

    fun receberCarta(carta: Carta) {
        mao.add(carta)
        println("$nome recebeu a carta: ${carta.nome}")
    }

    fun descartarCarta(carta: Carta) {
        mao.remove(carta)
        println("$nome descartou a carta: ${carta.nome}")
    }

    fun posicionarMonstro(cartaMonstro: CartaMonstro) {
        if (tabuleiro.size < 5) {
            tabuleiro.add(cartaMonstro)
            mao.remove(cartaMonstro)
            println("$nome posicionou o monstro ${cartaMonstro.nome} no tabuleiro.")
        } else {
            println("O tabuleiro está cheio! Não é possível posicionar mais monstros.")
        }
    }

    fun equiparMonstro(monstro: CartaMonstro, equipamento: CartaEquipamento) {
        // Equipamento pode ser implementado para aumentar as estatísticas do monstro
        println("$nome equipou ${monstro.nome} com ${equipamento.nome}")
    }

    fun resetarAtaques() {
        // Resetar os ataques dos monstros
        tabuleiro.forEach { it.estado = MonstroState.DEFESA }
    }

    fun escolherAcao(jogo: Jogo) {
        println("\nEscolha uma ação:")
        println("1) Posicionar um novo monstro no tabuleiro")
        println("2) Equipar um monstro com uma carta de equipamento")
        println("3) Descartar uma carta da mão")
        println("4) Realizar um ataque contra o oponente")
        println("5) Alterar o estado de um monstro (ataque/defesa)")
        println("6) Sair")

        val opcao = readLine()?.toIntOrNull()

        when (opcao) {
            1 -> posicionarMonstroAtravésDeEscolha()
            2 -> equiparMonstroAtravésDeEscolha()
            3 -> descartarCartaAtravésDeEscolha()
            4 -> realizarAtaque()
            5 -> alterarEstadoMonstro()
            6 -> println("Saindo da rodada.")
            else -> println("Opção inválida!")
        }
    }

    private fun posicionarMonstroAtravésDeEscolha() {
        val monstros = mao.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro da sua mão para posicionar:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)
            if (monstroEscolhido != null) {
                posicionarMonstro(monstroEscolhido)
            } else {
                println("Opção inválida ou monstro não encontrado.")
            }
        } else {
            println("Você não tem monstros na sua mão!")
        }
    }

    private fun equiparMonstroAtravésDeEscolha() {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        val equipamentos = mao.filterIsInstance<CartaEquipamento>()
        if (monstros.isNotEmpty() && equipamentos.isNotEmpty()) {
            println("Escolha um monstro para equipar:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolhaMonstro = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolhaMonstro?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                println("Escolha um equipamento para equipar:")
                equipamentos.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
                val escolhaEquipamento = readLine()?.toIntOrNull()
                val equipamentoEscolhido = equipamentos.getOrNull(escolhaEquipamento?.minus(1) ?: -1)

                if (equipamentoEscolhido != null) {
                    equiparMonstro(monstroEscolhido, equipamentoEscolhido)
                } else {
                    println("Equipamento inválido.")
                }
            } else {
                println("Monstro inválido.")
            }
        } else {
            println("Você não tem monstros ou equipamentos disponíveis!")
        }
    }

    private fun descartarCartaAtravésDeEscolha() {
        if (mao.isNotEmpty()) {
            println("Escolha uma carta para descartar:")
            mao.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolha = readLine()?.toIntOrNull()
            val cartaEscolhida = mao.getOrNull(escolha?.minus(1) ?: -1)
            if (cartaEscolhida != null) {
                descartarCarta(cartaEscolhida)
            } else {
                println("Opção inválida.")
            }
        } else {
            println("Você não tem cartas na mão!")
        }
    }

    private fun realizarAtaque() {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro para atacar:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                // Lógica para atacar o oponente, por exemplo, atacar o monstro adversário
                println("$nome está atacando com ${monstroEscolhido.nome}!")
            } else {
                println("Monstro inválido para atacar.")
            }
        } else {
            println("Você não tem monstros para atacar!")
        }
    }

    private fun alterarEstadoMonstro() {
        val monstros = tabuleiro.filterIsInstance<CartaMonstro>()
        if (monstros.isNotEmpty()) {
            println("Escolha um monstro para alterar o estado:")
            monstros.forEachIndexed { index, carta -> println("${index + 1}) ${carta.nome}") }
            val escolha = readLine()?.toIntOrNull()
            val monstroEscolhido = monstros.getOrNull(escolha?.minus(1) ?: -1)

            if (monstroEscolhido != null) {
                monstroEscolhido.alternarEstado()
            } else {
                println("Monstro inválido para alterar estado.")
            }
        } else {
            println("Você não tem monstros para alterar o estado!")
        }
    }
}
