include "struct.thrift"

struct UserProfileResponse {
  1: i32 responseCode,
  2: string etag,
  3: struct.UserProfile userProfile
}
