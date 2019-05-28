package com.jakdor.apapp.common.model.rating

data class RatingDisplayModel
constructor(var idRating: Int,
            var overallRating: Float,
            var ownerRating: Float,
            var locationRating: Float,
            var standardRating: Float,
            var priceRating: Float,
            var description: String,
            var login: String,
            var overallRatingStr: String)