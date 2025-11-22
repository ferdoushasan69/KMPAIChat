package com.smartai.kmp.data.mapper

import com.smartai.kmp.data.models.CandidateDto
import com.smartai.kmp.data.models.ContentDto
import com.smartai.kmp.data.models.GeminiResponseDto
import com.smartai.kmp.data.models.PartDto
import com.smartai.kmp.domain.model.Gemini

fun GeminiResponseDto.toGemini(): Gemini = Gemini(candidates = candidates.map { it.toCandidate() })

fun CandidateDto.toCandidate(): Gemini.Candidate = Gemini.Candidate(content.toContent())

fun ContentDto.toContent(): Gemini.Content = Gemini.Content(parts = parts.map { it.toPart() }, role)

fun PartDto.toPart(): Gemini.Part = Gemini.Part(text)