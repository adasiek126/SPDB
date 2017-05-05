#import <Foundation/Foundation.h>

@interface SPDBShortestPathRequest : NSObject

@property (nonatomic, strong) NSNumber *startingNodeId;
@property (nonatomic, strong) NSNumber *finishingNodeId;
@property (nonatomic, strong) NSString *publicTransport;
@property (nonatomic, strong) NSNumber *changeDuration;

+ (SPDBShortestPathRequest *)requestWithStartingNodeId:(NSNumber *)startingNodeId andFinishingNodeId:(NSNumber *)finishingNodeId;
+ (SPDBShortestPathRequest *)requestWithStartingNodeId:(NSNumber *)startingNodeId andFinishingNodeId:(NSNumber *)finishingNodeId withPublicTransport:(BOOL)publicTransport andChangeDuration:(NSNumber *)changeDuration;

@end
