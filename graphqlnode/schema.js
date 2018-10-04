import {
    GraphQLSchema,
    GraphQLObjectType,
    GraphQLInt,
    GraphQLString,
    GraphQLList,
} from 'graphql';

import fetch from 'node-fetch';

const API_URL = "http://127.0.0.1:8444";

const GenreType = new GraphQLObjectType({
    name: 'Genre',
    description: 'A possible category for a book',
    fields: () => ({
        id: {type: GraphQLString},
        name: {type: GraphQLString},
        books: {
            type: new GraphQLList(BookType),
            resolve: genre => fetch(`${API_URL}/books?genre-id=${genre.id}`).then(res => res.json())
        }
    })
});

const AuthorType = new GraphQLObjectType({
    name: 'Author',
    description: 'The writer of a book',
    fields: () => ({
        id: {type: GraphQLString},
        name: {type: GraphQLString},
        books: {
            type: new GraphQLList(BookType),
            resolve: author => fetch(`${API_URL}/books?author-id=${author.id}`).then(res => res.json())
        }
    })
});

const BookType = new GraphQLObjectType({
    name: 'Book',
    description: 'You know what a book is',
    fields: () => ({
        id: {type: GraphQLString},
        authorId: {type: GraphQLString},
        genreId: {type: GraphQLString},
        name: {type: GraphQLString},
        author: {
            type: AuthorType,
            resolve: book => fetch(`${API_URL}/authors?id=${book.authorId}`).then(res => res.json())
        },
        genre: {
            type: GenreType,
            resolve: book => fetch(`${API_URL}/genres?id=${book.genreId}`).then(res => res.json())
        }
    })
});

const QueryType = new GraphQLObjectType({
    name: 'Query',
    description: 'Available objects',
    fields: () => ({
        genres: {
            type: new GraphQLList(GenreType),
            resolve: () => fetch(`${API_URL}/genres`).then(res => res.json())
        },
        genre: {
            type: GenreType,
            args: {
                id: {type: GraphQLString}
            },
            resolve: (root, args) => fetch(`${API_URL}/genres?id=${args.id}`).then(res => res.json())
        },
        authors: {
            type: new GraphQLList(AuthorType),
            resolve: () => fetch(`${API_URL}/authors`).then(res => res.json())
        },
        author: {
            type: AuthorType,
            args: {
                id: {type: GraphQLString}
            },
            resolve: (root, args) => fetch(`${API_URL}/authors?id=${args.id}`).then(res => res.json())
        },
        books: {
            type: new GraphQLList(BookType),
            resolve: () => fetch(`${API_URL}/books`).then(res => res.json())
        },
        book: {
            type: BookType,
            args: {
                id: {type: GraphQLString}
            },
            resolve: (root, args) => fetch(`${API_URL}/books?id=${args.id}`).then(res => res.json())
        }
    })
});

export default new GraphQLSchema({
    query: QueryType
});
