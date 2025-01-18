import { ConfigurationChangeEvent, Event, TextEditor, TextEditorSelectionChangeEvent } from 'vscode';

//event subscribers update their information based on their event type
export interface EventSubscriber<T>{
    update(data: T): void;
}

//Class to store events and their subscribers, follows observer design pattern
export class EventRunner<T>{
    private event: Event<T>;
    private event_subscribers: EventSubscriber<T>[];
    
    constructor(event: Event<T>){
        this.event = event;
        this.event_subscribers = [];
    }
    
    //add a subscriber who wants to update when a given event is run
    public addSubscribers(...subscribers: EventSubscriber<T>[]){
        subscribers.forEach((subscriber)=>{
            if(!this.event_subscribers.includes(subscriber)){
                this.event_subscribers.push(subscriber);
            }
        })
    }

    //activate listening for events
    public listen(){
        this.event_subscribers.forEach((subscriber)=>{
            this.event.call(this, (e) => subscriber.update(e));
        });
    }
}

//manages subscribers of more than one event runner, provides registration service
export class EventManager{
    private event_runners: EventRunner<any>[];
    
    constructor(){
        this.event_runners = [];
    }

    //register an event runner with its subscribers to be listened to
    public register<T>(event: Event<T>, ...subscribers: EventSubscriber<T>[]){
        let event_runner = new EventRunner(event);

        if(this.event_runners.includes(event_runner)){
            this.addToExisting(event_runner, subscribers);
        }else{
            this.addNewEvent(event_runner, subscribers);
        }
    }

    //activate listening
    public listen(){
        this.event_runners.forEach((event_runner)=> {
            event_runner.listen();
        });
    }

    //helper function to add subscribers to an existing event
    private addToExisting(event: EventRunner<any>, subscribers: EventSubscriber<any>[]): void{
        let new_event = this.event_runners[this.event_runners.indexOf(event)];
        
        subscribers.forEach((subscriber) =>{
            new_event.addSubscribers(subscriber);
        })   
    }

    //helper function to add a new registered event (previously unregistered)
    private addNewEvent(event_runner: EventRunner<any>, subscribers: EventSubscriber<any>[]): void{
        subscribers.forEach((subscriber) =>{
            event_runner.addSubscribers(subscriber);
        });
        
        this.event_runners.push(event_runner);  
    }
}

//Checks if a value is a text editor (rudimentary, only use for event updating purposes)
export function isTextEditor(value: unknown): value is TextEditor | undefined{
    if(value === undefined) return true;
    
    //checking if it is an object
    if(!(value && typeof value === 'object' && !Array.isArray(value))) return false;
    
    if(!Object.hasOwn(value, "document")) return false;
    if(!Object.hasOwn(value, "selection")) return false;
    if(!Object.hasOwn(value, "selections")) return false;
    if(!Object.hasOwn(value, "visibleRanges")) return false;
    if(!Object.hasOwn(value, "options")) return false;
    if(!Object.hasOwn(value, "viewColumn")) return false;
    if(!Object.hasOwn(value, "edit")) return false;
    if(!Object.hasOwn(value, "insertSnippet")) return false;
    if(!Object.hasOwn(value, "setDecorations")) return false;
    if(!Object.hasOwn(value, "revealRange")) return false;
    if(!Object.hasOwn(value, "show")) return false;
    if(!Object.hasOwn(value, "hide")) return false;

    return true;
}

//Checks if a value is a text editor selection change event (rudimentary, only use for event updating purposes)
export function isTextEditorSelectionChangeEvent(value: unknown): value is TextEditorSelectionChangeEvent{
    if(!(value && typeof value === 'object' && !Array.isArray(value))) return false;
    if(!Object.hasOwn(value, "textEditor")) return false;
    if(!Object.hasOwn(value, "selections")) return false;
    if(!Object.hasOwn(value, "kind")) return false;

    return true;
}

//Checks if a value is a configuration change event (rudimentary, only use for event updating purposes)
export function isConfigurationChangeEvent(value: unknown): value is ConfigurationChangeEvent{
    if(!(value && typeof value === 'object' && !Array.isArray(value))) return false;
    if(!Object.keys(value).includes("affectsConfiguration")) return false;

    return true;
}