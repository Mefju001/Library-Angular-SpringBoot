export interface Review{
content?:string,
rating?:number,
createdAt?:Date,
user:UserDetailsReview
}
export interface UserDetailsReview{
    username:string
}