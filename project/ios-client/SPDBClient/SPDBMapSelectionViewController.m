#import "SPDBMapSelectionViewController.h"

@interface SPDBMapSelectionViewController ()

@end

@implementation SPDBMapSelectionViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self attachLongPressGestureRecognizer];
    [self setMapRegionToWarsaw];
    [self showSelectedPoint];
}

- (void)attachLongPressGestureRecognizer
{
    UILongPressGestureRecognizer *recognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPress:)];
    [recognizer setMinimumPressDuration:1.0];
    [self.mapView addGestureRecognizer:recognizer];
}

- (void)setMapRegionToWarsaw
{
    MKCoordinateRegion warsaw = MKCoordinateRegionMake(CLLocationCoordinate2DMake(52.232091, 21.006154), MKCoordinateSpanMake(0.04, 0.3));
    [self.mapView setRegion:warsaw animated:YES];
}

- (void)showSelectedPoint
{
    [self removeAnnotations];
    [self addAnnotationFromPoint:self.initialPoint];
}

- (void)removeAnnotations
{
    [self.mapView removeAnnotations:[self.mapView annotations]];
}

- (void)addAnnotationFromPoint:(NSValue *)point
{
    if (point != nil)
    {
        CGPoint pointStructure = [point CGPointValue];
        CLLocationCoordinate2D coordinate = CLLocationCoordinate2DMake(pointStructure.x, pointStructure.y);

        [self addAnnotationFromCoordinate:coordinate];
    }
}

- (void)addAnnotationFromCoordinate:(CLLocationCoordinate2D)coordinate
{
    MKPointAnnotation *pointAnnotation = [MKPointAnnotation new];
    pointAnnotation.coordinate = coordinate;
        
    [self.mapView addAnnotation:pointAnnotation];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void)handleLongPress:(UIGestureRecognizer *)gestureRecognizer
{
    if (gestureRecognizer.state == UIGestureRecognizerStateBegan)
    {
        CGPoint touchPoint = [gestureRecognizer locationInView:self.mapView];
        CLLocationCoordinate2D mapCoordinate = [self.mapView convertPoint:touchPoint toCoordinateFromView:self.mapView];
        
        [self removeAnnotations];
        [self addAnnotationFromCoordinate:mapCoordinate];
        [self.delegate didSelectPoint:[NSValue valueWithCGPoint:CGPointMake(mapCoordinate.latitude, mapCoordinate.longitude)] atIndex:self.pointIndex];
    }
}

@end
