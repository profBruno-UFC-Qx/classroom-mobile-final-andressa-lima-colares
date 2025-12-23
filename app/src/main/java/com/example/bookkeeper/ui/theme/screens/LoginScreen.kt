package com.example.bookkeeper.ui.theme.screens // <--- Corrigido para a pasta certa

import android.widget.Toast
import androidx.compose.foundation.Image // <--- Faltava esse import para a Logo funcionar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookkeeper.R // Se ficar vermelho, dê Alt+Enter aqui
import com.example.bookkeeper.viewmodel.BookViewModel

@Composable
fun LoginScreen(viewModel: BookViewModel) {
    // Estados dos campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") } // Só usado no cadastro

    // Controla se está na tela de Login ou Cadastro
    var isRegistering by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Cor de Papel Antigo
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- LOGO (Agora com o import correto) ---
        Image(
            painter = painterResource(id = R.drawable.logo_bookkeeper),
            contentDescription = "Logo BookKeeper",
            modifier = Modifier
                .size(180.dp) // Tamanho da logo
                .padding(bottom = 16.dp)
        )

        // Título Vintage
        Text(
            text = if (isRegistering) "Novo Leitor" else "Bem-vindo à Biblioteca",
            fontSize = 32.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo Nome (Só aparece se for cadastrar)
        if (isRegistering) {
            VintageTextField(value = name, onValueChange = { name = it }, label = "Seu Nome")
            Spacer(modifier = Modifier.height(16.dp))
        }

        VintageTextField(value = email, onValueChange = { email = it }, label = "Email")
        Spacer(modifier = Modifier.height(16.dp))

        VintageTextField(
            value = password,
            onValueChange = { password = it },
            label = "Senha",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão Principal
        Button(
            onClick = {
                if (isRegistering) {
                    // Tentar Cadastrar
                    viewModel.register(name, email, password) { success ->
                        if (!success) Toast.makeText(context, "Erro: Email já existe!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Tentar Logar
                    viewModel.login(email, password) { success ->
                        if (!success) Toast.makeText(context, "Email ou senha incorretos", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary, // Marrom
                contentColor = MaterialTheme.colorScheme.secondary // Dourado
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = if (isRegistering) "Assinar Livro de Registros" else "Abrir Biblioteca",
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto clicável para trocar de tela
        Text(
            text = if (isRegistering) "Já tem cadastro? Entre aqui." else "Primeira vez? Cadastre-se.",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { isRegistering = !isRegistering }
        )
    }
}

// Um campo de texto personalizado para ficar bonito
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VintageTextField(value: String, onValueChange: (String) -> Unit, label: String, isPassword: Boolean = false) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth(),
        // Configuração de Cores atualizada para Material3
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
        )
    )
}